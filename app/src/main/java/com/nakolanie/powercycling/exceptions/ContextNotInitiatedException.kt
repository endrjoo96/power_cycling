package com.nakolanie.powercycling.exceptions

import java.lang.RuntimeException

class ContextNotInitiatedException : RuntimeException("GameContext was not initiated. Use 'GameContext.init(...)' first.")