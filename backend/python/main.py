from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from pydantic import BaseModel
from fastapi.security import HTTPAuthorizationCredentials

from database import Base, engine, get_db
from sercurity import decode_token, security
from chatService import chat_with_history
import uvicorn

import model

app = FastAPI(title="Rescue Chatbot")

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
        user_message=req.message,
        user_name=payload["Name"],
        token=token
    )

    return ChatResponse(
        reply=reply,
        user_id=payload["id"],
        user_name=payload["Name"]
    )


@app.post("/api/test")
def test(
    credentials: HTTPAuthorizationCredentials = Depends(security),
    payload: dict = Depends(decode_token),
    db: Session = Depends(get_db),
):
    return { "user_id": payload["id"], "user_name": payload["Name"]}

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True
    )