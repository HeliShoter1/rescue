from sqlalchemy.orm import Session
from sqlalchemy import select

from langchain_core.messages import HumanMessage, SystemMessage, AIMessage

from config import MAX_HISTORY_MESSAGES, SYSTEM_PROMPT
from models import ChatMessage
from llm import llm


def get_recent_history(db: Session, user_id: int, limit: int = MAX_HISTORY_MESSAGES):
    """Lấy N tin nhắn gần nhất của user, sắp xếp theo thời gian tăng dần."""
    rows = (
        db.execute(
            select(ChatMessage)
            .where(ChatMessage.user_id == user_id)
            .order_by(ChatMessage.created_at.desc())
            .limit(limit)
        )
        .scalars()
        .all()
    )
    return list(reversed(rows))  # đảo lại để đúng thứ tự thời gian


def build_messages(history: list[ChatMessage], user_message: str):
    messages = [SystemMessage(content=SYSTEM_PROMPT)]
    for item in history:
        if item.role == "user":
            messages.append(HumanMessage(content=item.content))
        elif item.role == "assistant":
            messages.append(AIMessage(content=item.content))
    messages.append(HumanMessage(content=user_message))
    return messages


def save_message(db: Session, user_id: int, role: str, content: str):
    msg = ChatMessage(user_id=user_id, role=role, content=content)
    db.add(msg)
    db.commit()


def chat_with_history(db: Session, user_id: int, user_message: str) -> str:
    # 1. Lấy lịch sử gần nhất từ DB
    history = get_recent_history(db, user_id)

    # 2. Ghép context + câu hỏi mới
    messages = build_messages(history, user_message)

    # 3. Gọi LLM qua OpenRouter
    result = llm.invoke(messages)
    reply = result.content

    # 4. Lưu lại cả câu hỏi và câu trả lời vào DB
    save_message(db, user_id, "user", user_message)
    save_message(db, user_id, "assistant", reply)

    return reply