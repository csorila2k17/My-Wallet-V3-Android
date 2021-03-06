package piuk.blockchain.android.ui.charts

import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import info.blockchain.wallet.prices.data.PriceDatum
import io.reactivex.Observable
import org.amshove.kluent.`should be`
import org.amshove.kluent.mock
import org.junit.Before
import org.junit.Test
import piuk.blockchain.android.data.charts.ChartsDataManager
import piuk.blockchain.android.data.charts.TimeSpan
import piuk.blockchain.android.data.charts.models.ChartDatumDto
import piuk.blockchain.android.data.currency.CryptoCurrencies
import piuk.blockchain.android.util.ExchangeRateFactory
import piuk.blockchain.android.util.MonetaryUtil
import piuk.blockchain.android.util.PrefsUtil
import java.util.*

class ChartsPresenterTest {

    private lateinit var subject: ChartsPresenter
    private val chartsDataManager: ChartsDataManager = mock()
    private val exchangeRateFactory: ExchangeRateFactory = mock()
    private val prefsUtil: PrefsUtil = mock()
    private val view: ChartsView = mock()

    @Before
    @Throws(Exception::class)
    fun setUp() {

        subject = ChartsPresenter(
                chartsDataManager,
                exchangeRateFactory,
                prefsUtil
        )

        subject.initView(view)
    }

    @Test
    @Throws(Exception::class)
    fun `onViewReady success`() {
        // Arrange
        val chartData = ChartDatumDto(mock(PriceDatum::class))
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrencies.BTC)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY))
                .thenReturn(fiat)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC))
                .thenReturn(0)
        whenever(exchangeRateFactory.getLastBtcPrice(fiat)).thenReturn(13950.0)
        whenever(chartsDataManager.getMonthPrice(CryptoCurrencies.BTC, fiat))
                .thenReturn(Observable.just(chartData))
        // Act
        subject.onViewReady()
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.MONTH
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.TimeSpanUpdated(any(TimeSpan::class)))
        verify(view).updateChartState(ChartsState.Loading)
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.Data(any(), eq("US$")))
//        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getMonthPrice(CryptoCurrencies.BTC, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastBtcPrice(fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC)
        verifyNoMoreInteractions(prefsUtil)
    }

    @Test
    @Throws(Exception::class)
    fun `onViewReady failure`() {
        // Arrange
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrencies.BTC)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY))
                .thenReturn(fiat)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC))
                .thenReturn(0)
        whenever(exchangeRateFactory.getLastBtcPrice(fiat)).thenReturn(13950.0)
        whenever(chartsDataManager.getMonthPrice(CryptoCurrencies.BTC, fiat))
                .thenReturn(Observable.error(Throwable()))
        // Act
        subject.onViewReady()
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.MONTH
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.TimeSpanUpdated(any(TimeSpan::class)))
        verify(view).updateChartState(ChartsState.Loading)
        verify(view).updateChartState(ChartsState.Error)
//        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getMonthPrice(CryptoCurrencies.BTC, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastBtcPrice(fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC)
        verifyNoMoreInteractions(prefsUtil)
    }

    @Test
    @Throws(Exception::class)
    fun `setSelectedTimeSpan day`() {
        // Arrange
        val chartData = ChartDatumDto(mock(PriceDatum::class))
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrencies.BTC)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY))
                .thenReturn(fiat)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC))
                .thenReturn(0)
        whenever(exchangeRateFactory.getLastBtcPrice(fiat)).thenReturn(13950.0)
        whenever(chartsDataManager.getDayPrice(CryptoCurrencies.BTC, fiat))
                .thenReturn(Observable.just(chartData))
        // Act
        subject.selectedTimeSpan = TimeSpan.DAY
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.DAY
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.TimeSpanUpdated(any(TimeSpan::class)))
        verify(view).updateChartState(ChartsState.Loading)
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.Data(any(), eq("US$")))
//        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getDayPrice(CryptoCurrencies.BTC, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastBtcPrice(fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC)
        verifyNoMoreInteractions(prefsUtil)
    }

    @Test
    @Throws(Exception::class)
    fun `setSelectedTimeSpan week ETH`() {
        // Arrange
        val chartData = ChartDatumDto(mock(PriceDatum::class))
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrencies.ETHER)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY))
                .thenReturn(fiat)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC))
                .thenReturn(0)
        whenever(exchangeRateFactory.getLastEthPrice(fiat)).thenReturn(1281.78)
        whenever(chartsDataManager.getWeekPrice(CryptoCurrencies.ETHER, fiat))
                .thenReturn(Observable.just(chartData))
        // Act
        subject.selectedTimeSpan = TimeSpan.WEEK
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.WEEK
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.TimeSpanUpdated(any(TimeSpan::class)))
        verify(view).updateChartState(ChartsState.Loading)
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.Data(any(), eq("US$")))
//        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getWeekPrice(CryptoCurrencies.ETHER, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastEthPrice(fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC)
        verifyNoMoreInteractions(prefsUtil)
    }

    @Test
    @Throws(Exception::class)
    fun `setSelectedTimeSpan year ETH`() {
        // Arrange
        val chartData = ChartDatumDto(mock(PriceDatum::class))
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrencies.ETHER)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY))
                .thenReturn(fiat)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC))
                .thenReturn(0)
        whenever(exchangeRateFactory.getLastEthPrice(fiat)).thenReturn(1281.78)
        whenever(chartsDataManager.getYearPrice(CryptoCurrencies.ETHER, fiat))
                .thenReturn(Observable.just(chartData))
        // Act
        subject.selectedTimeSpan = TimeSpan.YEAR
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.YEAR
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.TimeSpanUpdated(any(TimeSpan::class)))
        verify(view).updateChartState(ChartsState.Loading)
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.Data(any(), eq("US$")))
//        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getYearPrice(CryptoCurrencies.ETHER, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastEthPrice(fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC)
        verifyNoMoreInteractions(prefsUtil)
    }

    @Test
    @Throws(Exception::class)
    fun `setSelectedTimeSpan all time BCH`() {
        // Arrange
        val chartData = ChartDatumDto(mock(PriceDatum::class))
        val fiat = "USD"
        whenever(view.cryptoCurrency).thenReturn(CryptoCurrencies.BCH)
        whenever(view.locale).thenReturn(Locale.UK)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY))
                .thenReturn(fiat)
        whenever(prefsUtil.getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC))
                .thenReturn(0)
        whenever(exchangeRateFactory.getLastBchPrice(fiat)).thenReturn(1281.78)
        whenever(chartsDataManager.getAllTimePrice(CryptoCurrencies.BCH, fiat))
                .thenReturn(Observable.just(chartData))
        // Act
        subject.selectedTimeSpan = TimeSpan.ALL_TIME
        // Assert
        subject.selectedTimeSpan `should be` TimeSpan.ALL_TIME
        verify(view, atLeastOnce()).cryptoCurrency
        verify(view, atLeastOnce()).locale
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.TimeSpanUpdated(any(TimeSpan::class)))
        verify(view).updateChartState(ChartsState.Loading)
        // TODO: Mockito currently doesn't play nicely with child classes of sealed parents
//        verify(view).updateChartState(ChartsState.Data(any(), eq("US$")))
//        verifyNoMoreInteractions(view)
        verify(chartsDataManager).getAllTimePrice(CryptoCurrencies.BCH, fiat)
        verifyNoMoreInteractions(chartsDataManager)
        verify(exchangeRateFactory).getLastBchPrice(fiat)
        verifyNoMoreInteractions(exchangeRateFactory)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY)
        verify(prefsUtil, atLeastOnce()).getValue(PrefsUtil.KEY_BTC_UNITS, MonetaryUtil.UNIT_BTC)
        verifyNoMoreInteractions(prefsUtil)
    }

}