from sqlalchemy.orm import Session
from sqlalchemy import select

from langchain_core.messages import HumanMessage, SystemMessage, AIMessage

from config import MAX_HISTORY_MESSAGES, SYSTEM_PROMPT
from model import ChatMessage
from llm import llm
from router import route_and_execute
from tool import build_tools


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
    return list(reversed(rows))


def build_messages(history: list[ChatMessage], user_name: str, user_message: str):
    messages = [SystemMessage(content=SYSTEM_PROMPT)]
    for item in history:
        if item.role == "user":
            messages.append(HumanMessage(content=item.content))
        elif item.role == "assistant":
            messages.append(AIMessage(content=item.content))
    messages.append(HumanMessage(content=f"[{user_name}]: {user_message}"))
    return messages


def save_message(db: Session, user_id: int, role: str, content: str):
    msg = ChatMessage(user_id=user_id, role=role, content=content)
    db.add(msg)
    db.commit()

def chat_with_history(db, user_id, user_message, user_name, token):
    history = get_recent_history(db, user_id)
    messages = build_messages(history, user_name, user_message)

    priority_tools, priority_map, fallback_tools, fallback_map = build_tools(token)
    reply = route_and_execute(
        llm, messages, priority_tools, priority_map, fallback_tools, fallback_map
    )

    save_message(db, user_id, "user", user_message)
    save_message(db, user_id, "assistant", reply)
    return reply