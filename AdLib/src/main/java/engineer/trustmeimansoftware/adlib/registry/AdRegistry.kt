package engineer.trustmeimansoftware.adlib.registry

import engineer.trustmeimansoftware.adlib.ad.Ad

/**
 * @class AdRegistry
 *
 * store and retrieve Ads
 * used for communicating between two activities, because
 * Activities' Bundle only allows primitive data types
 *
 * An Ad is stored in a key/value pattern, the key being the Ad's unique ID
 */
class AdRegistry() {
    private val store : HashMap<String, Ad> = HashMap()

    fun add(ad: Ad) {
        store[ad.getID()] = ad
    }

    fun get(key: String): Ad? {
        return store[key]
    }

    fun remove(key: String) {
        store.remove(key)
    }
}