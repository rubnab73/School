#!/bin/bash
# Local development setup script

set -e

echo "🚀 Setting up School Management System..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

# Start PostgreSQL container
echo "📦 Starting PostgreSQL container..."
docker-compose up -d postgres

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
for i in {1..30}; do
    if docker-compose exec -T postgres pg_isready -U schooluser &> /dev/null; then
        echo "✅ PostgreSQL is ready!"
        break
    fi
    echo "⏳ Waiting... ($i/30)"
    sleep 1
done

# Build the project
echo "🔨 Building the project..."
mvn clean install -DskipTests

echo ""
echo "✅ Setup complete!"
echo ""
echo "📝 Next steps:"
echo "  1. Start the application: mvn spring-boot:run"
echo "  2. Access the application at: http://localhost:8080"
echo "  3. Stop PostgreSQL: docker-compose down"
echo ""
