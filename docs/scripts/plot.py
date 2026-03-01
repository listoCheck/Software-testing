import csv
import math
import sys
from pathlib import Path

import matplotlib.pyplot as plt


def read_csv(path: Path, sep: str):
    xs, ys = [], []
    with path.open("r", encoding="utf-8") as f:
        reader = csv.reader(f, delimiter=sep)
        for row in reader:
            if len(row) < 2:
                continue
            try:
                x = float(row[0])
                y = float(row[1])
            except ValueError:
                continue
            if math.isnan(y) or math.isinf(y):
                continue
            xs.append(x)
            ys.append(y)
    return xs, ys


def main():
    if len(sys.argv) < 3:
        print("Usage: plot.py <csv_path> <out_png> [separator]")
        return 1
    csv_path = Path(sys.argv[1])
    out_path = Path(sys.argv[2])
    sep = sys.argv[3] if len(sys.argv) >= 4 else ";"

    xs, ys = read_csv(csv_path, sep)
    plt.figure(figsize=(8, 4.5))
    plt.plot(xs, ys, linewidth=1.5)
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig(out_path, dpi=160)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
