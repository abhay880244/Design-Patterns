package designpattern


enum class PROCESSOR_TYPE {
    TRAIN, AVAILABILITY, CLASS, QUOTA
}

interface Callback {
    fun onComplete(type : PROCESSOR_TYPE)
}
class DataProcessor(var builder: Builder) {
    companion object {
        class  Builder(var callback: Callback) {
            var type: PROCESSOR_TYPE? = null
            fun setProcessorType(type: PROCESSOR_TYPE) : Builder {
                this.type = type
                return this
            }
            fun build() : DataProcessor{
                return DataProcessor(this)
            }
        }
    }

    fun sort(){
        when (builder.type){
            PROCESSOR_TYPE.TRAIN->{}
            PROCESSOR_TYPE.AVAILABILITY->{}
            PROCESSOR_TYPE.CLASS->{}
            PROCESSOR_TYPE.QUOTA->{}
        }
    }
}

class BusinessLayer : Callback {
    fun main(){
        val trainProcessor = DataProcessor.Companion.Builder(this)
            .setProcessorType(PROCESSOR_TYPE.TRAIN)
            .build()

        trainProcessor.sort()

        val avlProcessor = DataProcessor.Companion.Builder(this)
            .setProcessorType(PROCESSOR_TYPE.AVAILABILITY)
            .build()

        avlProcessor.sort()
        val classProcessor = DataProcessor.Companion.Builder(this)
            .setProcessorType(PROCESSOR_TYPE.CLASS)
            .build()

        classProcessor.sort()
    }

    override fun onComplete(type: PROCESSOR_TYPE) {

    }
}
