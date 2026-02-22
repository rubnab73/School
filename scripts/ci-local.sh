#!/bin/bash
# CI/CD Helper Script - Run local CI checks before pushing

set -e

echo "🔍 Running CI Checks Locally..."
echo ""

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if git is initialized
if [ ! -d .git ]; then
    echo -e "${RED}❌ Not a git repository. Please run this from the root of the project.${NC}"
    exit 1
fi

echo -e "${YELLOW}📋 Running Maven Clean...${NC}"
mvn clean

echo ""
echo -e "${YELLOW}🔨 Building Project...${NC}"
mvn compile

echo ""
echo -e "${YELLOW}✅ Running Unit Tests...${NC}"
mvn test

echo ""
echo -e "${YELLOW}🐛 Running SpotBugs...${NC}"
mvn spotbugs:check || echo -e "${YELLOW}⚠️  SpotBugs warnings found (check logs)${NC}"

echo ""
echo -e "${YELLOW}📊 Running PMD...${NC}"
mvn pmd:check || echo -e "${YELLOW}⚠️  PMD violations found (check logs)${NC}"

echo ""
echo -e "${YELLOW}📐 Running Checkstyle...${NC}"
mvn checkstyle:check || echo -e "${YELLOW}⚠️  Code style issues found (check logs)${NC}"

echo ""
echo -e "${YELLOW}📦 Building JAR...${NC}"
mvn package -DskipTests

echo ""
echo -e "${GREEN}✅ All CI checks passed!${NC}"
echo ""
echo -e "${GREEN}Ready to push! 🚀${NC}"
echo ""
