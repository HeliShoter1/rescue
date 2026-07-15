from langchain_core.tools import tool
from sqlalchemy import select

from database import SessionLocal
from models import ChatMessage
from duckduckgo_search import DDGS

@tool
def getWeatherByLocation(token: str) -> str:
    """
    Lấy thông tin thời tiết hiện tại tại vị trí của người dùng.

    Args:
        token: JWT của người dùng (được router truyền vào, không hỏi người dùng).

    Returns:
        Chuỗi JSON chứa thông tin thời tiết hoặc thông báo lỗi.
    """

    response = requests.get(
        "http://localhost:8080/api/v1/weather/current",
        headers={
            "Authorization": f"Bearer {token}"
        },
        timeout=10
    )

    if response.status_code == 200:
        return response.text

    return (
        f"Không thể lấy thông tin thời tiết. "
        f"Status: {response.status_code}, Response: {response.text}"
    )

@tool
def searchRescueInformation(query: str) -> str:
    """
    Tìm kiếm thông tin trên Internet liên quan đến cứu trợ, thiên tai,
    sơ cứu, cứu hộ, thời tiết, hướng dẫn ứng phó và các thông tin khẩn cấp.

    Args:
        query: Nội dung cần tìm kiếm.

    Returns:
        Danh sách kết quả tìm kiếm.
    """

    try:
        with DDGS() as ddgs:
            results = list(ddgs.text(query, max_results=5))

        if not results:
            return "Không tìm thấy thông tin."

        output = []

        for i, result in enumerate(results, 1):
            output.append(
                f"{i}. {result['title']}\n"
                f"{result['body']}\n"
                f"{result['href']}\n"
            )

        return "\n".join(output)

    except Exception as e:
        return f"Lỗi khi tìm kiếm: {str(e)}"



# Danh sách tất cả tool để bind vào LLM
ALL_TOOLS = [getWeatherByLocation,searchRescueInformation]

# Map tên tool -> hàm thật, dùng để thực thi khi LLM yêu cầu gọi tool
TOOL_MAP = {t.name: t for t in ALL_TOOLS}