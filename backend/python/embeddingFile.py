import os
from pathlib import Path

from langchain_chroma import Chroma
from langchain_community.document_loaders import (
    DirectoryLoader,
    PyPDFLoader,
    TextLoader,
    Docx2txtLoader,
)
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_huggingface import HuggingFaceEmbeddings


# ==========================
# Cấu hình
# ==========================

DOCUMENT_DIR = "./documents"
CHROMA_DB_DIR = "./chroma_db"

embedding = HuggingFaceEmbeddings(
    model_name="sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2"
)

text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=800,
    chunk_overlap=150,
)


# ==========================
# Load tài liệu
# ==========================

documents = []

loaders = [
    DirectoryLoader(
        DOCUMENT_DIR,
        glob="**/*.pdf",
        loader_cls=PyPDFLoader,
        recursive=True,
        use_multithreading=False,
        show_progress=True,
    ),
    DirectoryLoader(
        DOCUMENT_DIR,
        glob="**/*.txt",
        loader_cls=TextLoader,
        loader_kwargs={"encoding": "utf-8"},
        recursive=True,
        use_multithreading=False,
        show_progress=True,
    ),
    DirectoryLoader(
        DOCUMENT_DIR,
        glob="**/*.docx",
        loader_cls=Docx2txtLoader,
        recursive=True,
        use_multithreading=False,
        show_progress=True,
    ),
]

for loader in loaders:
    try:
        documents.extend(loader.load())
    except Exception as e:
        print(e)


print(f"Đã load {len(documents)} tài liệu")


# ==========================
# Chia nhỏ
# ==========================

chunks = text_splitter.split_documents(documents)

print(f"Tạo {len(chunks)} chunks")


# ==========================
# Thêm metadata
# ==========================

for chunk in chunks:
    source = Path(chunk.metadata.get("source", ""))

    chunk.metadata["filename"] = source.name
    chunk.metadata["folder"] = source.parent.name


# ==========================
# Tạo Chroma
# ==========================

db = Chroma.from_documents(
    documents=chunks,
    embedding=embedding,
    persist_directory=CHROMA_DB_DIR,
)

print("Embedding hoàn tất!")
print(f"Lưu tại: {CHROMA_DB_DIR}")
print(f"Số chunk: {db._collection.count()}")