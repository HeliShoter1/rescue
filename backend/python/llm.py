from langchain_openai import ChatOpenAI

from config import OPENROUTER_API_KEY, OPENROUTER_BASE_URL, OPENROUTER_MODEL

llm = ChatOpenAI(
    model=OPENROUTER_MODEL,
    api_key=OPENROUTER_API_KEY,
    base_url=OPENROUTER_BASE_URL,
    temperature=0.7,
    default_headers={
        "HTTP-Referer": "http://localhost",
        "X-Title": "Rescue Chatbot",
    },
)