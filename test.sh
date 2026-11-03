#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p out
find src/main/java src/test/java -name '*.java' > /tmp/aitu-visitor-test-sources.txt
javac --release 17 -d out @/tmp/aitu-visitor-test-sources.txt
java -cp out kz.aitu.visitor.VisitorPatternTest
