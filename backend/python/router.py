from langchain_core.messages import AIMessage, ToolMessage

MAX_TOOL_LOOPS = 5  # tránh vòng lặp vô hạn nếu LLM cứ gọi tool liên tục

# Các dấu hiệu cho thấy tool ưu tiên không có dữ liệu hữu ích
NO_DATA_MARKERS = [
    "không tìm thấy",
    "không thể lấy",
    "lỗi khi",
    "không có thông tin",
]


def _is_no_data(result: str) -> bool:
    """Kiểm tra kết quả tool có phải dạng 'không có dữ liệu/lỗi' hay không."""
    text = result.lower()
    return any(marker in text for marker in NO_DATA_MARKERS)

def _run_tool_loop(llm, messages: list, all_tools: list, tool_map: dict):
    """
    Chạy 1 vòng bind_tools + thực thi tool.

    Args:
        llm: LLM chưa bind tool.
        messages: list messages hiện tại, được cập nhật trực tiếp (append).
        all_tools: danh sách tool cho vòng này.
        tool_map: map tên tool -> tool thật.

    Returns:
        Tuple (final_text, had_data):
        - final_text: câu trả lời cuối cùng (str), hoặc None nếu hết
          MAX_TOOL_LOOPS mà LLM vẫn chưa trả lời text.
        - had_data: True nếu có ít nhất 1 tool được gọi và kết quả không
          rơi vào NO_DATA_MARKERS (tức là có dữ liệu hữu ích).
    """
    llm_with_tools = llm.bind_tools(all_tools)
    had_data = False

    for _ in range(MAX_TOOL_LOOPS):
        ai_message: AIMessage = llm_with_tools.invoke(messages)
        messages.append(ai_message)

        tool_calls = getattr(ai_message, "tool_calls", None)
        if not tool_calls:
            return ai_message.content, had_data

        for call in tool_calls:
            tool_name = call["name"]
            tool_args = dict(call.get("args", {}))
            tool_id = call["id"]

            tool_fn = tool_map.get(tool_name)

            if tool_fn is None:
                result = f"Không tìm thấy tool '{tool_name}'."
            else:
                try:
                    result = tool_fn.invoke(tool_args)
                except Exception as e:
                    result = f"Lỗi khi thực thi tool '{tool_name}': {str(e)}"

            print(f"[DEBUG] Tool gọi: {tool_name} | args: {tool_args}")
            print(f"[DEBUG] Kết quả (300 ký tự đầu): {str(result)[:300]}")
            print(f"[DEBUG] _is_no_data = {_is_no_data(str(result))}")

            if not _is_no_data(str(result)):
                had_data = True

            messages.append(
                ToolMessage(content=str(result), tool_call_id=tool_id)
            )

    return None, had_data
def route_and_execute(
    llm,
    messages: list,
    priority_tools: list,
    priority_map: dict,
    fallback_tools: list,
    fallback_map: dict,
) -> str:
    """
    Bind tool vào LLM theo 2 giai đoạn:
    - Giai đoạn 1: chỉ cho LLM thấy priority_tools (thời tiết, dự báo,
      tài liệu nội bộ...). LLM chỉ được chọn trong nhóm này.
    - Giai đoạn 2: CHỈ khi giai đoạn 1 không cho ra dữ liệu hữu ích
      (không tool nào được gọi thành công, hoặc toàn bộ kết quả đều
      "không tìm thấy"/lỗi) -> mở thêm fallback_tools (web search) và
      chạy tiếp để LLM có thể tra cứu Internet.

    all_tools/tool_map được build riêng theo từng request (từ build_tools),
    không dùng biến global vì mỗi user có token khác nhau.

    Args:
        llm: LLM đã cấu hình (chưa bind tool).
        messages: danh sách messages hiện tại (list[BaseMessage]), sẽ được
                  cập nhật trực tiếp (append) trong quá trình xử lý.
        priority_tools: danh sách tool ưu tiên (nội bộ) cho request hiện tại.
        priority_map: map tên tool -> tool thật, tương ứng priority_tools.
        fallback_tools: danh sách tool dự phòng (web search) cho request hiện tại.
        fallback_map: map tên tool -> tool thật, tương ứng fallback_tools.

    Returns:
        Nội dung câu trả lời cuối cùng (str).
    """
    final_text, had_data = _run_tool_loop(llm, messages, priority_tools, priority_map)

    if final_text is not None and had_data:
        return final_text

    # Không có dữ liệu hữu ích từ tool ưu tiên -> mở thêm tool fallback (web search)
    all_tools = priority_tools + fallback_tools
    tool_map = {**priority_map, **fallback_map}

    final_text2, _ = _run_tool_loop(llm, messages, all_tools, tool_map)

    if final_text2 is not None:
        return final_text2

    return "Xin lỗi, hệ thống không thể xử lý yêu cầu sau nhiều lần gọi tool."