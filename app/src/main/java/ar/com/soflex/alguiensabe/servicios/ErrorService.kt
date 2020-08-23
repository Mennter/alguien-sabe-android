package ar.com.soflex.alguiensabe.servicios

import android.content.Context
import ar.com.soflex.alguiensabe.R
import ar.com.soflex.alguiensabe.dominio.ErrorDetalle
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class ErrorService(val context: Context) {

    fun handle(throwable: Throwable): ErrorDetalle {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    400 -> ErrorDetalle(context.getString(R.string.error_bad_request), throwable.code())
                    401 -> ErrorDetalle(context.getString(R.string.error_unautorized), throwable.code())
                    404 -> ErrorDetalle(context.getString(R.string.error_not_found), throwable.code())
                    500 -> ErrorDetalle(context.getString(R.string.error_internal_server), throwable.code())
                    else -> {
                        ErrorDetalle(context.getString(R.string.error_unknown), 0)
                    }
                }
            }
            is ConnectException  -> {
                ErrorDetalle(context.getString(R.string.error_connection), 0)
            }
            is SocketTimeoutException -> {
                ErrorDetalle(context.getString(R.string.error_connection), 0)
            }
            else -> {
                ErrorDetalle(context.getString(R.string.error_unknown), 0)
            }
        }

    }
}