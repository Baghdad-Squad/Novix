package com.baghdad.repository.exception

open class NetworkException(message: String? = null) : Exception(message)

class NoInternetNetworkException : NetworkException()
class SerializationNetworkException : NetworkException()
class RequestTimeoutNetworkException : NetworkException()
class TooManyRequestsNetworkException : NetworkException()
class ServerNetworkException : NetworkException()
class UnknownNetworkException : NetworkException()
class UnauthorizedNetworkException : NetworkException()
class ItemCreationFailedException(message: String) : NetworkException(message)