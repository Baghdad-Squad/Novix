package com.baghdad.repository.exception

sealed class NetworkException(message: String? = null) : Exception(message)

class NoInternetNetworkException : NetworkException("No internet connection")
class SerializationNetworkException : NetworkException("Failed to serialize/deserialize response")
class RequestTimeoutNetworkException : NetworkException("Request timed out")
class TooManyRequestsNetworkException : NetworkException("Too many requests")
class ServerNetworkException : NetworkException("Server error occurred")
class UnknownNetworkException : NetworkException("An unknown network error occurred")

