document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('select');
    var instances = M.FormSelect.init(elems, options);
  });

var example = new Vue({
  el: '#app',
  data: {
    input: '# hello',
    api: {},
    landmarks: [],
    temperature: 0.0,
    temperature_max: 0.0,
    temperature_min: 0.0,
    weather_icon: "",
    seen: false,
    city: "",
    cities: [
          {id: 0, name: 'Leeds'},
          {id: 1, name: 'York'},
          {id: 2, name: 'Harrogate'},
          {id: 3, name: 'Skipton'},
          {id: 4, name: 'Hull'},
          {id: 5, name: 'Doncaster'},
          {id: 6, name: 'Shipley'},
          {id: 7, name: 'Wakefield'},
          {id: 8, name: 'Whitby'},
          {id: 9, name: 'Leyburn'},
          {id: 10, name: 'Malton'},
          {id: 11, name: 'Carnforth'},
          {id: 12, name: 'Pickering'},
        ],
    selected: 0

  },
  computed: {
      listeners() {
        return {
          ...this.$listeners,
          click: this.handleClick
        }
      }
  },
  methods: {
      readFromAPI: function(event) {
        this.seen = false;
        // use ES6 fetch API, which return a promise
        this.city = this.cities[this.selected].name;
        promise = fetch(`http://localhost:8080/apicall/${this.city}`)
        .then(response =>
             response.json().then(data => ({
                 data: data,
                 status: response.status
             })
         ).then(res => {
             this.api = res.data;
             this.landmarks = this.api.landmark;
             this.temperature = this.api.weather.main.temp;
             this.temperature_max = this.api.weather.main.temp_max;
             this.temperature_min = this.api.weather.main.temp_min;
             this.weather_icon = this.api.weather.weather[0].icon;
             this.seen = true;
         }));
      }
  }
})