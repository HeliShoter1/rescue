from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage, SystemMessage, AIMessage

OPENROUTER_API_KEY = os.getenv("OPENROUTER_API_KEY", "PASTE_YOUR_OPENROUTER_KEY_HERE")
OPENROUTER_BASE_URL = "https://openrouter.ai/api/v1"
MODEL_NAME = os.getenv("OPENROUTER_MODEL", "openai/gpt-4o-mini") 

llm = ChatOpenAI(
    model=MODEL_NAME,
    api_key=OPENROUTER_API_KEY,
    base_url=OPENROUTER_BASE_URL,
    temperature=0.7,
    default_headers={
        "HTTP-Referer": "http://localhost",
        "X-Title": "Rescue Chatbot",
    },
)

SYSTEM_PROMPT = (
    "Bạn là trợ lý ảo của hệ thống điều phối cứu hộ khẩn cấp Rescue. "
    "Trả lời ngắn gọn, rõ ràng, ưu tiên thông tin an toàn cho người dùng."
)


class ChatRequest(BaseModel):
    message: str
    history: list[dict] = []  


class ChatResponse(BaseModel):
    reply: str
    user_id: int
    user_name: str