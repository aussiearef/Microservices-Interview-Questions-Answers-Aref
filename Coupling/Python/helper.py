
from datetime import datetime

def log(message):
    print(f"[LOG]: {message}")

def format_date(date):
    return date.strftime("%Y-%m-%d")

def is_valid_email(email):
    return "@" in email
