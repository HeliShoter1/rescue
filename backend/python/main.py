from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from pydantic import BaseModel

from database import Base, engine, get_db
from security import decode_token
from chat_service import chat_with_history

import models  # noqa: F401  (đảm bảo model được đăng ký trước khi create_all)

app = FastAPI(title="Rescue Chatbot")

# Tạo bảng nếu chưa tồn tại
Base.metadata.create_all(bind=engine)


class ChatRequest(BaseModel):
    message: str


class ChatResponse(BaseModel):
    reply: str
    user_id: int
    user_name: str


@app.post("/api/chat")
def chat(
    req: ChatRequest,
    credentials: HTTPAuthorizationCredentials = Depends(security),
    payload: dict = Depends(decode_token),
    db: Session = Depends(get_db),
):
    token = credentials.credentials

    reply = chat_with_history(
        db=db,
        user_id=payload["id"],
        message=req.message,
        token=token
    )