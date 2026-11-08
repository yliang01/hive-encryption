# AGENTS.md — hive-encryption

- This Maven aggregator contains `hive-encryption-starter` (encryption API and Spring Boot integration) and `hive-encryption-cli` (standalone command-line entry point).
- Read the child module instructions before changing either module. Keep the CLI and starter using the same encryption implementation and preserve the existing encrypted configuration format used by `hive-oss` and `hive-release`.
- Treat plaintext, keys, salts, IVs, and encrypted configuration as secrets: never commit real values or log them. Do not change the wire/file format without updating every consumer and its tests.
- Build the reactor from this directory with `mvn test`; the optional native executable profile is `-Pnative` and requires a compatible GraalVM toolchain.
