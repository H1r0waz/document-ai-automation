import re
from datetime import date
from decimal import Decimal
from fastapi import FastAPI, File, HTTPException, UploadFile
from pypdf import PdfReader

app = FastAPI(title="Document extractor")

@app.post("/extract")
async def extract(file: UploadFile = File(...)):
    if file.content_type != "application/pdf":
        raise HTTPException(415, "Solo se aceptan archivos PDF")
    reader = PdfReader(file.file)
    text = "\n".join(page.extract_text() or "" for page in reader.pages)
    if not text.strip():
        raise HTTPException(422, "El PDF no contiene texto extraíble; necesita OCR")
    return {
        "supplier": find(r"(?:Proveedor|Razón social)\s*[:#-]?\s*(.+)", text),
        "invoiceNumber": find(r"(?:Factura|N[°ºo]?)\s*[:#-]?\s*([A-Z0-9-]{4,})", text),
        "invoiceDate": parse_date(find(r"(?:Fecha)\s*[:#-]?\s*(\d{1,2}[/-]\d{1,2}[/-]\d{2,4})", text)),
        "total": parse_total(find(r"(?:Total)\s*[:$-]?\s*[$]?\s*([\d.,]+)", text)),
    }

def find(pattern: str, text: str):
    match = re.search(pattern, text, re.IGNORECASE)
    return match.group(1).strip() if match else None

def parse_date(value):
    if not value:
        return None
    day, month, year = re.split(r"[/-]", value)
    year = "20" + year if len(year) == 2 else year
    try:
        return date(int(year), int(month), int(day)).isoformat()
    except ValueError:
        return None

def parse_total(value):
    if not value:
        return None
    normalized = value.replace(".", "").replace(",", ".")
    try:
        return str(Decimal(normalized))
    except Exception:
        return None
