package sk.letsdream.helperMethods


class HexMethods {
    fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuffer()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }

}