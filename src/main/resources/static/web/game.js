function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

Vue.component('board', {
    template: `
        <div>
            Soy un board
        </div>
    `
});

var app = new Vue({
    el: "#app",
    data: {
        gp: null
    },
    created() {

    },
    methods: {

    }
});