package dk.spilpind.sms.core.model

/**
 * Various methods that's useful when dealing with the core models
 */
object ModelHelper {

    /**
     * Converts a user to a privileged user with [roles]
     */
    fun User.toPrivileged(roles: List<UserRole.Simple>) = User.Privileged(
        userId = userId,
        name = name,
        appleEmail = appleEmail,
        googleEmail = googleEmail,
        roles = roles
    )

    /**
     * Defines if the tournament is the current stick league based on the tags - this can therefore change from season
     * to season
     */
    val Tournament.isCurrentStickLeague: Boolean
        get() = tags.contains("stick-league-current")

    /**
     * Converts a raw event to a simple event
     */
    fun Event.Raw.toDetailedEvent(): Event.Simple {
        val baseInfo = Event.Simple.BaseInfo(
            eventId = eventId,
            gameId = gameId,
            teamId = teamId,
            time = time,
            refereeId = refereeId,
            created = created
        )

        val type = Event.Type.values().firstOrNull { type -> type.typeId == typeId }
            ?: throw IllegalStateException("Got unknown event type: $typeId")

        return when (type) {
            Event.Type.Points -> Event.Points(baseInfo = baseInfo, points = points ?: 0) // TODO - Log if null points
            Event.Type.Death -> Event.Death(baseInfo = baseInfo)
            Event.Type.LiftSuccess -> Event.LiftSuccess(baseInfo = baseInfo)
            Event.Type.GameStart -> Event.Timing(baseInfo = baseInfo, timingType = Event.Timing.TimingType.GameStart)
            Event.Type.GameEnd -> Event.Timing(baseInfo = baseInfo, timingType = Event.Timing.TimingType.GameEnd)
            Event.Type.PauseStart -> Event.Timing(baseInfo = baseInfo, timingType = Event.Timing.TimingType.PauseStart)
            Event.Type.PauseEnd -> Event.Timing(baseInfo = baseInfo, timingType = Event.Timing.TimingType.PauseEnd)
            Event.Type.FaultClick -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.Click)
            Event.Type.FaultBackLift -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.BackLift)
            Event.Type.FaultRoll -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.Roll)
            Event.Type.FaultOut -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.Out)
            Event.Type.FaultCatch -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.Catch)
            Event.Type.FaultWicketDirect ->
                Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.WicketDirect)
            Event.Type.FaultWicketShin -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.WicketShin)
            Event.Type.FaultHitCatch -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.HitCatch)
            Event.Type.FaultCrossingDefenceLine ->
                Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.CrossingDefenceLine)
            Event.Type.SwitchForce -> Event.Switch(baseInfo = baseInfo, switchType = Event.Switch.SwitchType.Force)
            Event.Type.SwitchTime -> Event.Switch(baseInfo = baseInfo, switchType = Event.Switch.SwitchType.Time)
            Event.Type.SwitchDeaths -> Event.Switch(baseInfo = baseInfo, switchType = Event.Switch.SwitchType.Death)
        }
    }

    /**
     * Converts a raw game to a simple game
     */
    fun Game.Raw.toSimpleGame() = Game.Simple(
        gameId = gameId,
        tournamentId = tournamentId,
        teamAId = teamAId,
        teamBId = teamBId,
        state = Game.State.values().firstOrNull { state -> state.identifier == gameState }
            ?: throw IllegalStateException("Got unknown game state: $gameState"),
        teamAPoints = teamAPoints,
        teamBPoints = teamBPoints,
        description = description,
        teamJoinInviteCode = teamJoinInviteCode
    )

    /**
     * Converts a simple game to a detailed game. It is expected that the provided parameters matches the properties of
     * the simple game
     */
    fun Game.Simple.toDetailedGame(tournament: Tournament, teamA: Team?, teamB: Team?) = Game.Detailed(
        gameId = gameId,
        tournament = tournament,
        teamA = teamA,
        teamB = teamB,
        state = state,
        teamAPoints = teamAPoints,
        teamBPoints = teamBPoints,
        description = description,
        teamJoinInviteCode = teamJoinInviteCode
    )

    /**
     * Converts a detailed game to a simple game
     */
    fun Game.Detailed.toSimpleGame() = Game.Simple(
        gameId = gameId,
        tournamentId = tournamentId,
        teamAId = teamAId,
        teamBId = teamBId,
        state = state,
        teamAPoints = teamAPoints,
        teamBPoints = teamBPoints,
        description = description,
        teamJoinInviteCode = teamJoinInviteCode
    )

    /**
     * Creates an event id from the integer
     */
    fun Int.toEventId() = Event.Id(this)

    /**
     * Creates a game id from the integer
     */
    fun Int.toGameId() = Game.Id(this)

    /**
     * Creates a pending request id from the integer
     */
    fun Int.toPendingRequestId() = PendingRequest.Id(this)

    /**
     * Creates a team id from the integer
     */
    fun Int.toTeamId() = Team.Id(this)

    /**
     * Creates a tournament id from the integer
     */
    fun Int.toTournamentId() = Tournament.Id(this)

    /**
     * Creates a user id from the integer
     */
    fun Int.toUserId() = User.Id(this)

    /**
     * Creates a user role id from the integer
     */
    fun Int.toUserRoleId() = UserRole.Id(this)

}
