#  Algorithms & Complexity
## Assignment 2: Entropy of Language & Commit Owners

### Description
This project contains two Java programs (plus a bonus feature):

1. NGramGameAlbanian
- Computes n-gram (n = 0…10) entropies on Albanian text files.
- Handles special letters (ë, ç, etc.) by normalizing and filtering to letters only.
- Prints entropy in bits, total token counts, and top/bottom 5 tokens for each n.
- Bonus: Also builds bigram models for Albanian vs. English and classifies an unknown sentence via log-probabilities with add-one smoothing.

2. WeldStringGame
- Reads an `employees.txt` file mapping ID → (Surname, FirstName).
- Given a “welded” string of IDs, uses recursive backtracking to find all valid decompositions.
- Selects and prints the decomposition with the most commits, plus the total count of valid decompositions.

### Prerequisites
- **Java JDK 11** (or later)
- (Optional) IntelliJ IDEA or any Java-compatible IDE


### Project Layout

assignment2Algorithm/
├─ .idea/ ← IntelliJ project files
├─ out/ ← IntelliJ build output
├─ src/
│ ├─ data/
│ │ └─ alb.txt ← Albanian text for n-grams
│ ├─ data2/
│ │ ├─ alb2.txt ← Albanian corpus for language ID
│ │ ├─ english.txt ← English corpus for language ID
│ │ └─ input.txt ← Input sentence to classify
│ ├─ dataEmployee/
│ │ └─ employee.txt ← ID,Surname,FirstName for WeldStringGame
│ ├─ NGramGameAlbanian.java ← Part I + bonus
│ └─ WeldStringGame.java ← Part II
├─ .gitignore ← ignored files
├─ assignment2Algorithm.iml ← IntelliJ module file
└─ README.md ← this project guide

### Run

1. Entropy & Language Identifier
   java -cp src NGramGameAlbanian
   Scans every .txt in data/ for n-gram entropy (n=0…10).

Then reads data2/alb.txt, data2/english.txt, and data2/input.txt to classify the input sentence as Albanian or English.


2. Commit Owners (Weld Decoder)
   java -cp src WeldStringGame employees.txt <weldString>

Replace <weldString> with your concatenated ID string, e.g.:

java -cp src WeldStringGame employees.txt 15101341021117

### Example Output

N-GRAM ENTROPY
=== Processing N-grams: alb.txt ===
n = 0-gram
Entropy: 4.807355 bits
Total tokens: 28
Top 5 tokens:
a : 1
b : 1
c : 1
d : 1
e : 1
Bottom 5 tokens:
v : 1
w : 1
x : 1
y : 1
z : 1

Commit Owners
Best decomposition (most commits):
1: Hysa Leo
5: Meta Arjeta
10: Kosova Driton
1: Hysa Leo
3: Mehmetaj Elisa
4: Berisha Arlind
10: Kosova Driton
2: Sinani Sibora
1: Hysa Leo
1: Hysa Leo
17: Veseli Luan


=== Language Identifier ===
Input sentence: The text is going to be in English.

LogProb (Albanian): -236.49578996407593
LogProb (English) : -201.95369535209224
Likely Language: English

### Notes

~Make sure your working directory is the project root so that relative paths (data/, data2/, employees.txt) resolve correctly.
~To add more sample texts, just drop additional .txt files into data/ or data2/.
~Feel free to open/run in IntelliJ by creating two Application run configurations pointing to NGramGameAlbanian and WeldStringGame.

### License & Academic Honesty

All code and documentation are our original work. No unauthorized sources were used without citation.

