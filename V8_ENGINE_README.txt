DUMOK FM V8 NEW ENGINE CORE
This package introduces a separate com.dumok.fm.engine simulation core.
It is intentionally isolated from the legacy MainActivity match loop.
Core: MatchEngine, TeamAI, Player motor, Ball physics, ContactSystem.
Validation: standalone Java compile + 5000-tick smoke test passed.
Next integration step is to bind SmoothPitch rendering/input to MatchEngine snapshots, then remove the legacy loop.
