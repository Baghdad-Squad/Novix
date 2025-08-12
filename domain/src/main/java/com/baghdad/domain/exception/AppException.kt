package com.baghdad.domain.exception

open class AppException() : Exception()
class UnknownException() : AppException()

open class NetworkException() : AppException()
class UnAuthorizedException() : NetworkException()
class NoInternetException() : NetworkException()
class UnKnownNetworkException() : NetworkException()

open class LocalDataBaseException() : AppException()
class StorageFullException() : LocalDataBaseException()

open class AuthenticationException() : AppException()
class InValidPasswordException() : AuthenticationException()
class EmptyFieldException() : AuthenticationException()
class InValidUserCredentialException() : AuthenticationException()