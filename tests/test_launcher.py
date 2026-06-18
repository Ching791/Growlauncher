import unittest
from pathlib import Path

from launcher import build_launch_command, launch_growtopia


class LauncherTests(unittest.TestCase):
    def test_build_launch_command_returns_absolute_path(self) -> None:
        command = build_launch_command("launcher.py")
        self.assertEqual(1, len(command))
        self.assertTrue(Path(command[0]).is_absolute())

    def test_launch_raises_when_file_missing(self) -> None:
        with self.assertRaises(FileNotFoundError):
            launch_growtopia("/definitely/missing/growtopia-clone")

    def test_launch_calls_popen_with_expected_values(self) -> None:
        launcher_file = Path(__file__).resolve().parents[1] / "launcher.py"
        captured = {}

        def fake_popen(command, cwd=None):
            captured["command"] = command
            captured["cwd"] = cwd
            return "started"

        result = launch_growtopia(str(launcher_file), popen=fake_popen)

        self.assertEqual("started", result)
        self.assertEqual([str(launcher_file.resolve())], captured["command"])
        self.assertEqual(str(launcher_file.parent.resolve()), captured["cwd"])


if __name__ == "__main__":
    unittest.main()
