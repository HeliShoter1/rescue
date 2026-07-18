import requests
from langchain_core.tools import tool
from ddgs import DDGS

from config import CHROMA_DB_DIR, embedding, vectorstore

def make_weather_tool(token: str):
    """
    Factory tạo tool lấy thời tiết, token được đóng gói sẵn (closure)
    nên KHÔNG xuất hiện trong schema tool -> LLM không cần và không thể
    tự điền token, tránh việc LLM bịa/hỏi lại người dùng.
    """

    @tool
    def getWeatherByLocation() -> str:
        """
        Lấy thông tin thời tiết hiện tại tại vị trí của người dùng đang đăng nhập.
        Không cần tham số, hệ thống tự xác định người dùng và vị trí.
        """
        try:
            response = requests.get(
                "http://localhost:9090/api/v1/weather/current",
                headers={"Authorization": f"Bearer {token}"},
                timeout=10,
            )
        except requests.RequestException as e:
            return f"Lỗi khi gọi API thời tiết: {str(e)}"

        if response.status_code == 200:
            return response.text

        return (
            f"Không thể lấy thông tin thời tiết. "
            f"Status: {response.status_code}, Response: {response.text}"
        )

    return getWeatherByLocation

import requests
from langchain_core.tools import tool


def make_weather_forecast_tool(token: str):
    """
    Factory tạo tool lấy dự báo thời tiết, token được đóng gói sẵn (closure)
    nên KHÔNG xuất hiện trong schema tool -> LLM không cần và không thể
    tự điền token.
    """

    @tool
    def getWeatherForecast() -> str:
        """
        Lấy thông tin dự báo thời tiết (nhiều ngày tới) tại vị trí của
        người dùng đang đăng nhập. Không cần tham số, hệ thống tự xác
        định người dùng và vị trí.

        Returns:
            Chuỗi JSON chứa thông tin dự báo thời tiết hoặc thông báo lỗi.
        """
        try:
            response = requests.get(
                "http://localhost:9090/api/v1/weather/forecast",
                headers={"Authorization": f"Bearer {token}"},
                timeout=10,
            )
        except requests.RequestException as e:
            return f"Lỗi khi gọi API dự báo thời tiết: {str(e)}"

        if response.status_code == 200:
            return response.text

        return (
            f"Không thể lấy thông tin dự báo thời tiết. "
            f"Status: {response.status_code}, Response: {response.text}"
        )

    return getWeatherForecast

@tool
def searchRescueInformation(query: str) -> str:
    """
    Tìm kiếm thông tin trên Internet liên quan đến cứu trợ, thiên tai,
    sơ cứu, cứu hộ, hướng dẫn ứng phó và các thông tin khẩn cấp.
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


@tool
def searchRescueDocuments(query: str) -> str:
    """
    Tìm kiếm thông tin trong kho tài liệu nội bộ (quy trình ứng phó thảm họa,
    sơ cứu, cứu hộ...) đã được embedding sẵn trong ChromaDB. Ưu tiên dùng tool
    này TRƯỚC khi dùng tool tìm kiếm Internet, vì đây là tài liệu chính thống
    của hệ thống Rescue.

    Args:
        query: Nội dung/câu hỏi cần tìm kiếm.

    Returns:
        Các đoạn văn bản liên quan nhất kèm nguồn (tên file), hoặc thông báo
        nếu không tìm thấy.
    """
    try:
        results = vectorstore.similarity_search(query, k=4)
    except Exception as e:
        return f"Lỗi khi truy vấn ChromaDB: {str(e)}"

    if not results:
        return "Không tìm thấy thông tin liên quan trong tài liệu nội bộ."

    output = []
    for i, doc in enumerate(results, 1):
        filename = doc.metadata.get("filename", "không rõ nguồn")
        output.append(f"{i}. (Nguồn: {filename})\n{doc.page_content}\n")

    return "\n".join(output)    

def build_tools(token: str):
    priority_tools = [
        make_weather_tool(token),
        make_weather_forecast_tool(token),
        searchRescueDocuments,
    ]
    priority_map = {t.name: t for t in priority_tools}

    fallback_tools = [searchRescueInformation]
    fallback_map = {t.name: t for t in fallback_tools}

    return priority_tools, priority_map, fallback_tools, fallback_map