# Hive Encryption CLI

A secure and efficient command-line tool for encrypting and decrypting files or strings using the AES algorithm.

---

## 🔐 Password

The CLI will prompt for a password interactively at runtime to avoid leakage.

---

## 🧭 Usage

```
encrypt|decrypt [options]
```

### Options

| Parameter       | Short | Description                                       | Required      |
| --------------- | ----- | ------------------------------------------------- | ------------- |
| `--input-file`  | `-i`  | Input file path                                   | One of two    |
| `--input`       | `-x`  | Plain string (encrypt) or hex string (decrypt)    | One of two    |
| `--output-file` | `-o`  | Output file path (required when input is file)    | Conditionally |
| `--iv`          | `-v`  | Initialization vector (optional, default: random) | No            |
| `--salt`        | `-s`  | Salt (optional, default: randomly generated)      | No            |

---

## ✅ Examples

### 📤 Encrypting Data

* Encrypt a string and print to console (hex):

```bash
encrypt -x data
```

* Encrypt a string and save to file:

```bash
encrypt -x data -o C:\path\to\encrypted
```

* Encrypt a file and save the result to another file:

```bash
encrypt -i C:\path\to\data -o C:\path\to\encrypted
```

### 📥 Decrypting Data

* Decrypt hex string and print result to console:

```bash
decrypt -x CC90B19A275CEEA89A94E62E838AC6C8
```

* Decrypt hex string and save output to file:

```bash
decrypt -x CC90B19A275CEEA89A94E62E838AC6C8 -o C:\path\to\data
```

* Decrypt an encrypted file and save plaintext:

```bash
decrypt -i C:\path\to\encrypted -o C:\path\to\data
```

---

## 🚀 Quick Start

### Compile

```bash
mvn clean package
```

### Run

```bash
hive-encryption-cli.exe encrypt -x hello
```

---

## 🛠 Supported Environments

* Java version: 21+
* OS: Windows
* GraalVM Native Image support