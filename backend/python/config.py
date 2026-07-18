import base64
import os

from dotenv import load_dotenv
from langchain_chroma import Chroma
from langchain_huggingface import HuggingFaceEmbeddings

load_dotenv()

# ====== JWT ======
JWT_SECRET_BASE64 = os.getenv("JWT_SECRET_BASE64")
JWT_SECRET_KEY = base64.b64decode(JWT_SECRET_BASE64)
JWT_ALGORITHM = os.getenv("JWT_ALGORITHM")

# ====== PostgreSQL ======
DATABASE_URL = os.getenv(
    "DATABASE_URL"
)

# ====== OpenRouter ======
OPENROUTER_API_KEY = os.getenv("OPENROUTER_API_KEY")
OPENROUTER_BASE_URL = "https://openrouter.ai/api/v1"
OPENROUTER_MODEL = os.getenv("OPENROUTER_MODEL", "openai/gpt-4o-mini")

# ====== Chatbot ======
MAX_HISTORY_MESSAGES = int(os.getenv("MAX_HISTORY_MESSAGES", "20"))

SYSTEM_PROMPT = (
    "Bạn là trợ lý ảo của hệ thống điều phối cứu hộ khẩn cấp Rescue. "
    "Trả lời ngắn gọn, rõ ràng, ưu tiên thông tin an toàn cho người dùng.\n\n"
    "QUY TẮC BẮT BUỘC VỀ TOOL:\n"
    "- Với câu hỏi về thời tiết hiện tại: gọi tool `getWeatherByLocation`.\n"
    "- Với câu hỏi về dự báo thời tiết (ngày mai, tuần tới,...): gọi tool "
    "`getWeatherForecast`.\n"
    "- Với câu hỏi liên quan đến sơ cứu, cứu hộ, thiên tai, quy trình ứng phó: "
    "LUÔN gọi tool `searchRescueDocuments` TRƯỚC TIÊN để tìm trong tài liệu nội bộ, "
    "KỂ CẢ khi bạn nghĩ mình đã biết câu trả lời.\n"
    "- CHỈ gọi tool `searchRescueInformation` (tìm kiếm Internet) khi và chỉ khi "
    "`getWeatherByLocation`, `getWeatherForecast`, hoặc `searchRescueDocuments` "
    "không trả về kết quả, báo lỗi, hoặc dữ liệu trả về không liên quan đến câu hỏi. "
    "Không dùng tìm kiếm Internet nếu 3 tool trên đã có đủ thông tin.\n"
    "- Không tự trả lời các câu hỏi về sơ cứu/cứu hộ/thời tiết chỉ bằng kiến thức "
    "có sẵn nếu chưa gọi tool tương ứng.\n"
    "- Nếu tool trả về lỗi hoặc không có dữ liệu, hãy nói rõ là không lấy được "
    "thông tin, không tự bịa ra kết quả.\n\n"
    "ĐỊNH DẠNG TIN NHẮN NGƯỜI DÙNG:\n"
    "- Tin nhắn người dùng có định dạng `[tên người dùng]: nội dung`. "
    "Phần `[tên người dùng]` là tên của người đang chat với bạn, không phải "
    "một phần nội dung câu hỏi. Bạn có thể dùng tên này để xưng hô cho thân "
    "thiện, nhưng không đọc lại nguyên văn định dạng đó trong câu trả lời."
    "-Không trả lời những câu hỏi không liên quan đến cứu hộ, thiên tai, sơ cứu, cứu trợ, thời tiết, "
)  

CHROMA_DB_DIR = "./chroma_db"

embedding = HuggingFaceEmbeddings(
    model_name="sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2"
)

vectorstore = Chroma(
    persist_directory=CHROMA_DB_DIR,
    embedding_function=embedding,
)