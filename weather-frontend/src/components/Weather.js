import React, { useState, useEffect } from 'react'; // Import Reactu a hookov pre stav a efekt
import axios from 'axios'; // Import knižnice axios na HTTP požiadavky
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer
} from 'recharts'; // Import komponentov z knižnice recharts pre kreslenie grafov

const Weather = () => { //Definovanie môjho komponentu
  // Stav pre aktuálne počasie
  const [currentWeather, setCurrentWeather] = useState(null);
  // Stav pre históriu alebo predpoveď počasia 
  const [weatherHistory, setWeatherHistory] = useState([]);
  // Stav na indikáciu načítavania dát
  const [loading, setLoading] = useState(true);
  // Stav pre prípadné chyby pri načítaní dát
  const [error, setError] = useState(null);

  // Efekt, ktorý sa spustí raz po načítaní komponentu
  useEffect(() => {
    // Asynchrónna funkcia (neblokuje vykonávanie programu) na získanie dát z backendu
    const fetchWeatherData = async () => {
      try {
        // Posielam GET požiadavku na endpoint /api/weather
        const response = await axios.get('/api/weather');
        // Nastavím aktuálne počasie zo získaných dát
        setCurrentWeather(response.data.current);
        // Nastavím predpoveď počasia
        setWeatherHistory(response.data.history);
      } catch (err) {
        // Ak nastane chyba, nastavím chybovú správu
        setError('Nepodarilo sa načítať dáta o počasí');
        console.error('API Error:', err); // Logovanie chyby do konzoly
      } finally {
        // Ukončím stav načítavania, nech sa zobrazí obsah alebo chyba
        setLoading(false);
      }
    };

    fetchWeatherData(); // Zavolám túto funkciu hneď po načítaní komponentu
  }, []); // Prázdne pole zabezpečí, že efekt sa spustí len raz (dik Reddit)

  // Ak nastala chyba, zobrazíme chybovú správu
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="weather-container">
      {/* Názov miesta z aktuálneho počasia */}
      <h2>Počasie v {currentWeather.name}</h2>

      <div className="weather-main">
        {/* Ikona počasia podľa API */}
        <img
          src={`https://openweathermap.org/img/wn/${currentWeather.weather[0].icon}@2x.png`}
          alt={currentWeather.weather[0].description}
        />
        <div className="temperature">
          {/* Aktuálna teplota zaokrúhlená na celé stupne */}
          {Math.round(currentWeather.main.temp)}°C
          <div className="feels-like">
            {/* Pocitová teplota */}
            Pocitovo: {Math.round(currentWeather.main.feels_like)}°C
          </div>
        </div>
      </div>

      <div className="weather-details">
        {/* detaily počasia */}
        <p><strong>Stav:</strong> {currentWeather.weather[0].description}</p>
        <p><strong>Vlhkosť:</strong> {currentWeather.main.humidity}%</p>
        <p><strong>Vietor:</strong> {currentWeather.wind.speed} m/s</p>
        <p><strong>Tlak:</strong> {currentWeather.main.pressure} hPa</p>
      </div>

      {/* zobrazím graf */}
      {weatherHistory.length > 0 && (
        <div className="weather-chart">
          <h3>Predpoveď teploty na ďalšie dni</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={weatherHistory}>
              {/* Mriežka na pozadí grafu */}
              <CartesianGrid stroke="#ccc" strokeDasharray="5 5" />
              {/* Os X s časovými popiskami */}
              <XAxis
                dataKey="dt"
                tickFormatter={(dt) =>
                  // Formátujem čas na hodiny a minúty
                  new Date(dt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
                }
                interval={7} // Zobrazujem len 7 popiskov
              />
              {/* Os Y s jednotkami stupňov */}
              <YAxis unit="°C" />
              {/* Tooltip (cooltip) zobrazuje detailné info ked na neho prejdem myskou */}
              <Tooltip
                labelFormatter={(dt) =>
                  new Date(dt).toLocaleString([], {
                    weekday: 'short',
                    hour: '2-digit',
                    minute: '2-digit',
                  })
                }
              />
              {/* Čiara znázorňujúca teplotu */}
              <Line type="monotone" dataKey="temp" stroke="#8884d8" strokeWidth={2} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      )}
    </div>
  );
};

export default Weather;
