package ua.hospes.cryptogateway.ui.configs.edit

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.domain.interactors.SaveBotConfig
import ua.cryptogateway.domain.models.BotConfigModel
import ua.cryptogateway.extensions.combine
import ua.cryptogateway.util.AppCoroutineDispatchers
import ua.hospes.cryptogateway.ui.common.formatDouble

@Inject
class ConfigEditViewModel(
    //@Assisted savedStateHandle: SavedStateHandle, TODO: Library not working properly with Assisted injection yet
    dispatchers: AppCoroutineDispatchers,
    private val saveBotConfig: SaveBotConfig,
) : ViewModel() {
    private val dispatcher = dispatchers.io

    private val _events = MutableSharedFlow<ConfigEditViewEvent>()
    val events: SharedFlow<ConfigEditViewEvent> = _events

    private val config = MutableStateFlow<BotConfigModel?>(null)

    private val title = config.map { it?.let { "${it.baseAsset}_${it.quoteAsset}" } }
    private val _baseAsset = MutableStateFlow(TextFieldValue())
    private val _quoteAsset = MutableStateFlow(TextFieldValue())
    private val _side = MutableStateFlow<Order.Side>(Order.Side.Sell)
    private val _fond = MutableStateFlow(TextFieldValue())
    private val _startPrice = MutableStateFlow(TextFieldValue())
    private val _priceStep = MutableStateFlow(TextFieldValue())
    private val _biasPrice = MutableStateFlow(TextFieldValue())
    private val _minSize = MutableStateFlow(TextFieldValue())
    private val _orderSize = MutableStateFlow(TextFieldValue())
    private val _sizeStep = MutableStateFlow(TextFieldValue())
    private val _orderAmount = MutableStateFlow(TextFieldValue())
    private val _priceForce = MutableStateFlow(false)
    private val _market = MutableStateFlow(false)
    private val _basePrec = MutableStateFlow(TextFieldValue())
    private val _quotePrec = MutableStateFlow(TextFieldValue())
    private val _active = MutableStateFlow(false)
    private val _inProgress = MutableStateFlow(false)

    val state = combine(
        title, _baseAsset, _quoteAsset, _side,
        _fond, _startPrice, _priceStep, _biasPrice,
        _minSize, _orderSize, _sizeStep, _orderAmount,
        _priceForce, _market, _basePrec, _quotePrec,
        _active,
    ) { title, base, quote, side,
        fond, startPrice, priceStep, biasPrice,
        minSize, orderSize, sizeStep, orderAmount,
        priceForce, market, basePrec, quotePrec,
        active ->

        ConfigEditViewState(
            title = title, baseAsset = base, quoteAsset = quote, side = side,
            fond = fond, startPrice = startPrice, priceStep = priceStep, biasPrice = biasPrice,
            minSize = minSize, orderSize = orderSize, sizeStep = sizeStep, orderAmount = orderAmount,
            priceForce = priceForce, market = market, basePrec = basePrec, quotePrec = quotePrec,
            active = active,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ConfigEditViewState.Init,
    )


    init {
        viewModelScope.launch(dispatcher) {
            config.collectLatest {
                it?.baseAsset?.let { _baseAsset.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.quoteAsset?.let { _quoteAsset.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.side?.let { _side.value = it }
                it?.fond?.formatDouble()?.let { _fond.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.startPrice?.formatDouble()?.let { _startPrice.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.priceStep?.formatDouble()?.let { _priceStep.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.biasPrice?.formatDouble()?.let { _biasPrice.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.minSize?.formatDouble()?.let { _minSize.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.orderSize?.toString()?.let { _orderSize.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.sizeStep?.formatDouble()?.let { _sizeStep.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.orderAmount?.toString()?.let { _orderAmount.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.priceForce?.let { _priceForce.value = it }
                it?.market?.let { _market.value = it }
                it?.basePrec?.toString()?.let { _basePrec.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.quotePrec?.toString()?.let { _quotePrec.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
                it?.active?.let { _active.value = it }
            }
        }
    }

    fun initConfig(config: BotConfigModel?) = with(this.config) { value = config }

    fun onBaseAssetChange(text: TextFieldValue) = with(_baseAsset) { value = text }
    fun onQuoteAssetChange(text: TextFieldValue) = with(_quoteAsset) { value = text }
    fun onSideSelect(side: Order.Side) = with(_side) { value = side }
    fun onFondChange(text: TextFieldValue) = with(_fond) { value = text }
    fun onStartPriceChange(text: TextFieldValue) = with(_startPrice) { value = text }
    fun onPriceStepChange(text: TextFieldValue) = with(_priceStep) { value = text }
    fun onBiasPriceChange(text: TextFieldValue) = with(_biasPrice) { value = text }
    fun onMinSizeChange(text: TextFieldValue) = with(_minSize) { value = text }
    fun onOrderSizeChange(text: TextFieldValue) = with(_orderSize) { value = text }
    fun onSizeStepChange(text: TextFieldValue) = with(_sizeStep) { value = text }
    fun onOrderAmountChange(text: TextFieldValue) = with(_orderAmount) { value = text }
    fun onPriceForceChange(force: Boolean) = with(_priceForce) { value = force }
    fun onMarketChange(market: Boolean) = with(_market) { value = market }
    fun onBasePrecChange(text: TextFieldValue) = with(_basePrec) { value = text }
    fun onQuotePrecChange(text: TextFieldValue) = with(_quotePrec) { value = text }
    fun onActiveChange(active: Boolean) = with(_active) { value = active }
    fun onSave() {
        if (_inProgress.value) return
        _inProgress.value = true
        viewModelScope.save()
    }


    private fun CoroutineScope.save() = launch(dispatcher) {
        _inProgress.value = true

        saveBotConfig(
            baseAsset = _baseAsset.value.text,
            quoteAsset = _quoteAsset.value.text,
            side = _side.value,
            fond = _fond.value.text.toDoubleOrNull() ?: 0.0,
            startPrice = _startPrice.value.text.toDoubleOrNull() ?: 0.0,
            priceStep = _priceStep.value.text.toDoubleOrNull() ?: 0.0,
            biasPrice = _biasPrice.value.text.toDoubleOrNull() ?: 0.0,
            minSize = _minSize.value.text.toDoubleOrNull() ?: 0.0,
            orderSize = _orderSize.value.text.toIntOrNull() ?: 0,
            sizeStep = _sizeStep.value.text.toDoubleOrNull() ?: 0.0,
            orderAmount = _orderAmount.value.text.toIntOrNull() ?: 0,
            priceForce = _priceForce.value,
            market = _market.value,
            basePrec = _basePrec.value.text.toIntOrNull() ?: 0,
            quotePrec = _quotePrec.value.text.toIntOrNull() ?: 0,
            active = _active.value,
        )
            .onSuccess { _events.emit(ConfigEditViewEvent.ConfigSaved) }
            .onFailure { Log.warn(tag = TAG, throwable = it) { "Failed while saving config" } }

    }.also { it.invokeOnCompletion { _inProgress.value = false } }


    companion object {
        private const val TAG = "ConfigEditViewModel"
    }
}