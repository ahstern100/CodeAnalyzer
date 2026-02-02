# CodeAnalyzer 🚀

A powerful Java tool that performs structural code analysis, AI-powered summarization, and semantic search. It parses your repository, generates summaries for every file and method, and allows you to search through your codebase using natural language.

## ✨ Features
* **Structural Parsing**: Deep analysis of Java files using `JavaParser`.
* **AI Summarization**: Generates logical summaries for classes and methods (including getters/setters).
* **Semantic Search**: Find code by "meaning" rather than just keywords using vector embeddings.
* **Local Embeddings**: Fast, local vector generation via ONNX.

---

## 🛠 Prerequisites
* **JDK 11** or higher.
* **Maven 3.6+**.
* **Groq API Key**: Obtain one from [Groq Console](https://console.groq.com/).

---

## ⚙️ Environment Configuration

To keep your credentials secure, the application reads the API key from your system environment variables.

### Set the API Key
**Windows (Command Prompt):**
```cmd
setx GROQ_API_KEY "your_actual_api_key_here"

```

**Linux / macOS:**

```bash
export GROQ_API_KEY="your_actual_api_key_here"

```

---

## 🚀 How to Run

### 1. Build the project

Navigate to the root directory and run:

```bash
mvn clean install

```

### 2. Run the application

Run the following command to launch the UI:

```bash
mvn exec:java -Dexec.mainClass="process.EntryPoint"

```

---

## 🖥 Using the UI

Once the application starts, configure the following settings for optimal performance:

1. **Language Model**: Select either **GROQ** or **OLLAMA**.
* *Note: Other options are currently placeholders and will not function.*


2. **Embedding Model**: Select **ONNX**.
* *Note: This is required for local high-speed vector processing.*


3. **Project Path**: Enter the absolute path to the Java repository you wish to analyze.

---

## 📦 Dependencies

This project leverages:

* **LangChain4j**: For seamless AI model integration.
* **JavaParser**: For source code structure analysis.
* **Jackson/JSON**: For robust data handling.
* **ONNX Runtime**: For local, high-performance embeddings.

---

## ⚠️ Important Note

This tool is designed for educational and productivity purposes. Ensure your `GROQ_API_KEY` is active and you have a stable internet connection if using cloud-based models.
