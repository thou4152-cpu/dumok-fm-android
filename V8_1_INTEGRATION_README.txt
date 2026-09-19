DUMOK FM V8.1 INTEGRATION
- SmoothPitch owns the new MatchEngine and mirrors its 22 player + ball coordinates.
- The legacy Handler animation loop is not started while V8 is authoritative.
- onDraw steps V8 and schedules the next render frame.
- Old MainActivity match code remains in source for UI compatibility, but is no longer the active frame driver.
- Standalone V8 engine Java compile + 5000-tick smoke test passed.
- This archive had no Gradle wrapper, so a full Android Gradle compile could not be executed in this environment.
