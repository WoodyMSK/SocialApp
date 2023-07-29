import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Асинхронный вызов в [Dispatchers.IO] потоке
 * Приостановка программы в точке вызова и ожидая результат
 * Используется для обработки файлов, интернет запросов
 *
 * @param errorHandler - если нужен свой собственный обработчик ошибок
 */
suspend fun <T> withContextIO(
    errorHandler: CoroutineExceptionHandler? = null,
    block: suspend CoroutineScope.() -> T
): T {
    return withContext(concatenateContexts(Dispatchers.IO, errorHandler), block)
}

private fun concatenateContexts(
    coroutineContext1: CoroutineContext,
    coroutineContext2: CoroutineContext?,
): CoroutineContext {
    if (coroutineContext2 == null) return coroutineContext1
    return coroutineContext1 + coroutineContext2
}