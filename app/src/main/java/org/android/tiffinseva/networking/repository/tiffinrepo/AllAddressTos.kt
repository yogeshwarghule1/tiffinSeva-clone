package org.android.tiffinseva.networking.repository.tiffinrepo

data class AllAddressResponseTO(
        val addresses: List<AddressesItem>? = null
)

data class AddressesItem(
        val country: String? = null,
        val pincode: String? = null,
        val city: String? = null,
        val addressLine1: String? = null,
        val addressLine2: String? = null,
        val id: Int? = null,
        val state: String? = null,
        val landmark: String? = null,
        val isSelected: Boolean = false
)
