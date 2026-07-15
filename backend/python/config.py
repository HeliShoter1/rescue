import base64
import os

from dotenv import load_dotenv

load_dotenv()

# ====== JWT ======
JWT_SECRET_BASE64 = os.getenv("JWT_SECRET_BASE64", "PASTE_YOUR_BASE64_SECRET_HERE")
JWT_SECRET_KEY = base64.b64decode(JWT_SECRET_BASE64)
JWT_ALGORITHM = "HS256"

# ====== PostgreSQL ======
DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "postgresql+psycopg2://postgres:postgres@localhost:5432/rescue_chatbot",
)

# ====== OpenRouter ======
OPENROUTER_API_KEY = os.getenv("OPENROUTER_API_KEY", "PASTE_YOUR_OPENROUTER_KEY_HERE")
OPENROUTER_BASE_URL = "https://openrouter.ai/api/v1"
OPENROUTER_MODEL = os.getenv("OPENROUTER_MODEL", "openai/gpt-4o-mini")

# ====== Chatbot ======
MAX_HISTORY_MESSAGES = int(os.getenv("MAX_HISTORY_MESSAGES", "10"))

SYSTEM_PROMPT = (
    "Bạn là trợ lý ảo của hệ thống điều phối cứu hộ khẩn cấp Rescue. "
    "Trả lời ngắn gọn, rõ ràng, ưu tiên thông tin an toàn cho người dùng."
)   