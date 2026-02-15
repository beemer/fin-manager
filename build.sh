#!/bin/bash
# Quick Start Script for fin-manager Backend

echo "=== fin-manager Backend Setup ==="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Please install Maven first."
    exit 1
fi

echo "✅ Maven found"
echo ""

# Clean previous builds
echo "🧹 Cleaning previous builds..."
mvn clean

# Compile
echo "🔨 Compiling..."
mvn compile
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed"
    exit 1
fi
echo "✅ Compilation successful"
echo ""

# Run tests
echo "🧪 Running tests..."
mvn test
if [ $? -ne 0 ]; then
    echo "⚠️  Some tests failed - check output"
fi
echo ""

# Package
echo "📦 Packaging JAR..."
mvn package -DskipTests

if [ -f "target/fin-manager.jar" ]; then
    echo "✅ JAR created: target/fin-manager.jar"
    echo ""
    echo "🚀 To run the server:"
    echo "   java -jar target/fin-manager.jar"
    echo ""
    echo "📡 Server will be available at: http://localhost:8080"
    echo ""
    echo "💡 Example API calls:"
    echo "   curl http://localhost:8080/api/health"
    echo "   curl http://localhost:8080/api/categories"
else
    echo "❌ JAR creation failed"
    exit 1
fi
