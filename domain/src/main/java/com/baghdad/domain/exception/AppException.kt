package com.baghdad.domain.exception

open class AppException() : Exception()
open class ApiException() : AppException()
open class LocalDataBaseException() : AppException()


class NoInternetException() : ApiException()
class SerializationException() : ApiException()
class RequestTimeoutException() : ApiException()
class TooManyRequestsException() : ApiException()
class ServerException() : ApiException()
class NotFoundSearchException() : ApiException()

class DataBaseCorruptException() : LocalDataBaseException()
class StorageFullException() : LocalDataBaseException()
class NotFoundException() : LocalDataBaseException()
