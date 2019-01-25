package com.grosslicht.patricksbot.minecraft



class MinecraftPingReply {

    /**
     * @return the MOTD
     */
    val description: Description? = null
    /**
     * @return @{link Players}
     */
    val players: Players? = null
    /**
     * @return @{link Version}
     */
    val version: Version? = null
    /**
     * @return Base64 encoded favicon image
     */
    val favicon: String? = null

    inner class Description {
        /**
         * @return Server description text
         */
        val text: String? = null
    }

    inner class Players {
        /**
         * @return Maximum player count
         */
        val max: Int = 0
        /**
         * @return Online player count
         */
        val online: Int = 0
        /**
         * @return List of some players (if any) specified by server
         */
        val sample: List<Player>? = null
    }

    inner class Player {
        /**
         * @return Name of player
         */
        val name: String? = null
        /**
         * @return Unknown
         */
        val id: String? = null

    }

    inner class Version {
        /**
         * @return Version name (ex: 13w41a)
         */
        val name: String? = null
        /**
         * @return Protocol version
         */
        val protocol: Int = 0
    }

}
