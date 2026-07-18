from langchain_community.document_loaders import DirectoryLoader, PyPDFLoader

DOCUMENT_DIR = "./documents"

loader = DirectoryLoader(
    DOCUMENT_DIR,
    glob="*.pdf",
    loader_cls=PyPDFLoader,
    recursive=True,
    show_progress=True,
    silent_errors=False,   # để lỗi hiện ra thay vì bị nuốt
    use_multithreading=False,
)
docs = loader.load()
print("Số document load qua DirectoryLoader:", len(docs))