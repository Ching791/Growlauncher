from __future__ import annotations

import os
import subprocess
from pathlib import Path


def default_executable_path() -> str:
    env_path = os.getenv("GROWTOPIA_CLONE_PATH")
    if env_path:
        return env_path
    executable = "GrowtopiaClone.exe" if os.name == "nt" else "growtopia-clone"
    return str(Path(__file__).resolve().parent / executable)


def build_launch_command(executable_path: str) -> list[str]:
    return [str(Path(executable_path).expanduser().resolve())]


def launch_growtopia(executable_path: str, popen=subprocess.Popen) -> subprocess.Popen:
    command = build_launch_command(executable_path)
    executable = Path(command[0])
    if not executable.exists():
        raise FileNotFoundError(f"Growtopia clone not found: {executable}")
    return popen(command, cwd=str(executable.parent))


def create_app():
    import tkinter as tk
    from tkinter import messagebox

    root = tk.Tk()
    root.title("Growlauncher")
    root.geometry("460x140")
    root.resizable(False, False)

    path_var = tk.StringVar(value=default_executable_path())

    tk.Label(root, text="Growtopia clone executable path:").pack(anchor="w", padx=12, pady=(12, 4))
    tk.Entry(root, textvariable=path_var, width=64).pack(fill="x", padx=12)

    def on_launch() -> None:
        try:
            launch_growtopia(path_var.get())
            messagebox.showinfo("Growlauncher", "Growtopia clone launched.")
        except Exception as exc:  # noqa: BLE001
            messagebox.showerror("Growlauncher", str(exc))

    tk.Button(root, text="Launch", command=on_launch).pack(anchor="e", padx=12, pady=12)
    return root


if __name__ == "__main__":
    create_app().mainloop()
