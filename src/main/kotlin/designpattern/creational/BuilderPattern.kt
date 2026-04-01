package designpattern.creational

/**
 * Builder Pattern Explanation:
 *
 * The Builder pattern is used to construct complex objects step-by-step.
 * Instead of passing many parameters to a constructor, you use a fluent
 * interface to set optional properties one at a time, then call build()
 * to create the final object.
 *
 * Benefits:
 * - Improves readability when objects have many optional parameters
 * - Allows setting properties in a flexible order
 * - Makes it easy to understand which parameters are being set
 * - Separates construction logic from the object itself
 */

enum class PROCESSOR_TYPE {
    TRAIN, AVAILABILITY, CLASS, QUOTA
}

interface Callback {
    fun onComplete(type : PROCESSOR_TYPE)
}
class DataProcessor(
    var type: PROCESSOR_TYPE? = null,
    var callback: Callback? = null,
    var batchSize: Int = 100,
    var timeout: Long = 30000,
    var retryCount: Int = 3,
    var isAsync: Boolean = false
) {
    companion object {
        class Builder(var callback: Callback) {
            var type: PROCESSOR_TYPE? = null
            var batchSize: Int = 100
            var timeout: Long = 30000
            var retryCount: Int = 3
            var isAsync: Boolean = false

            fun setProcessorType(type: PROCESSOR_TYPE): Builder {
                this.type = type
                return this
            }

            fun setBatchSize(size: Int): Builder {
                this.batchSize = size
                return this
            }

            fun setTimeout(timeoutMs: Long): Builder {
                this.timeout = timeoutMs
                return this
            }

            fun setRetryCount(count: Int): Builder {
                this.retryCount = count
                return this
            }

            fun setAsync(async: Boolean): Builder {
                this.isAsync = async
                return this
            }

            fun build(): DataProcessor {
                return DataProcessor(
                    type = this.type,
                    callback = this.callback,
                    batchSize = this.batchSize,
                    timeout = this.timeout,
                    retryCount = this.retryCount,
                    isAsync = this.isAsync
                )
            }
        }
    }

    fun sort() {
        when (type) {
            PROCESSOR_TYPE.TRAIN -> {}
            PROCESSOR_TYPE.AVAILABILITY -> {}
            PROCESSOR_TYPE.CLASS -> {}
            PROCESSOR_TYPE.QUOTA -> {}
            null -> {}
        }
    }
}

class BusinessLayer : Callback {
    fun main() {
        val trainProcessor = DataProcessor.Companion.Builder(this)
            .setProcessorType(PROCESSOR_TYPE.TRAIN)
            .setBatchSize(500)
            .setTimeout(60000)
            .setRetryCount(5)
            .setAsync(true)
            .build()

        trainProcessor.sort()

        val avlProcessor = DataProcessor.Companion.Builder(this)
            .setProcessorType(PROCESSOR_TYPE.AVAILABILITY)
            .setBatchSize(200)
            .setTimeout(45000)
            .build()

        avlProcessor.sort()

        val classProcessor = DataProcessor.Companion.Builder(this)
            .setProcessorType(PROCESSOR_TYPE.CLASS)
            .setRetryCount(10)
            .setAsync(true)
            .build()

        classProcessor.sort()
    }

    override fun onComplete(type: PROCESSOR_TYPE) {

    }
}

