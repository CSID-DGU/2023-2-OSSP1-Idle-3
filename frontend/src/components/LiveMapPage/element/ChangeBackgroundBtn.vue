<!-- <template>
  <v-sheet color="transparent">
    <v-sheet
      max-width="500"
      color="transparent"
      width="100%"
      class="d-flex flex-column justify-end align-end"
      style="
        position: fixed;
        margin: 0 auto;
        left: 0;
        right: 0;
        bottom: 82%;
        padding: 0 15px 15px 0;
      "
    >
      <input type="radio" name="rdo" id="map" v-model="mapType" />
      <input type="radio" name="rdo" id="tile" v-model="mapType" />
      <div class="switch" :class="{ active: mapType }">
        <label for="map">MAP</label>
        <label for="tile">TILE</label>
        <span></span>
      </div>
    </v-sheet>
  </v-sheet>
</template>

<script>
export default {
  name: "ChangeBackground",
  props: {},
  data() {
    return {
      mapType: true,
    };
  },
  methods: {},
};
</script>

<style scoped>
.switch {
  position: absolute;
  /* position: fixed; */
  /* top: 50%; */
  /* left: 50%; */
  /* width: 150px; */
  width: 130px;
  /* height: 50px; */
  height: 30px;
  text-align: center;
  /* margin: -30px 0 0 -75px; */
  margin: 0 0 15px 15px; /* 변경된 여백 설정 */
  background: var(--roulette-col-4);
  transition: all 0.2s ease;
  border-radius: 25px;
}

.switch span {
  position: absolute;
  /* position: fixed; */
  width: 20px;
  /* width: 3px; */
  height: 4px;
  top: 50%;
  left: 50%;
  margin: -2px 0px 0px -4px;
  background: white;
  display: block;
  transform: rotate(-45deg);
  transition: all 0.2s ease;
  border-radius: 2px;
}

.switch span:after {
  content: "";
  display: block;
  position: absolute;
  /* position: fixed; */
  width: 4px;
  height: 12px;
  margin-top: -8px;
  background: white;
  transition: all 0.2s ease;
  border-radius: 2px;
}

input[type="radio"] {
  display: none;
}

.switch label {
  cursor: pointer;
  color: rgba(0, 0, 0, 0.3);
  width: 60px;
  line-height: 50px;
  transition: all 0.2s ease;
}

label[for="map"] {
  position: absolute;
  /* position: fixed; */
  left: 0px;
  /* height: 20px; */
  bottom: -10px;
  font-size: small;
}

label[for="tile"] {
  position: absolute;
  /* position: fixed; */
  right: 0px;
  bottom: -10px;
  font-size: small;
}

#tile:checked ~ .switch {
  background: var(--roulette-col-2);
}

#tile:checked ~ .switch span {
  background: white;
  margin-left: -8px;
}

#tile:checked ~ .switch span:after {
  background: white;
  height: 20px;
  margin-top: -8px;
  margin-left: 8px;
}

#map:checked ~ .switch label[for="map"] {
  color: white;
}

#tile:checked ~ .switch label[for="tile"] {
  color: white;
}
</style> -->
<template>
  <div
    max-width="500"
    color="transparent"
    width="100%"
    class="d-flex flex-column justify-end align-end"
    style="
      position: fixed;
      margin: 0 auto;
      left: 0;
      right: 0;
      bottom: 90%;
      padding: 0 15px 15px 0;
      z-index: 2;
    "
  >
    <input
      type="radio"
      name="rdo"
      id="map"
      v-model="mapType"
      @change="changeMapType"
    />
    <input
      type="radio"
      name="rdo"
      id="tile"
      v-model="mapType"
      @change="changeMapType"
    />
    <div class="switch" :class="{ active: mapType }">
      <label for="map">MAP</label>
      <label for="tile">TILE</label>
      <span></span>
    </div>
  </div>
</template>

<script>
export default {
  name: "ChangeBackgroundBtn",
  data() {
    return {
      mapType: "map",
    };
  },
  methods: {
    changeMapType() {
      // Handle map type change logic here
    },
  },
};
</script>

<style scoped>
.switch {
  position: relative;
  width: 130px;
  height: 30px;
  text-align: center;
  margin-bottom: 15px;
  background: var(--roulette-col-4);
  transition: all 0.2s ease;
  border-radius: 25px;
}

.switch span {
  position: absolute;
  width: 20px;
  height: 4px;
  top: 50%;
  left: 50%;
  margin: -2px 0px 0px -4px;
  background: white;
  display: block;
  transform: rotate(-45deg);
  transition: all 0.2s ease;
  border-radius: 2px;
}

.switch span:after {
  content: "";
  display: block;
  position: absolute;
  width: 4px;
  height: 12px;
  margin-top: -8px;
  background: white;
  transition: all 0.2s ease;
  border-radius: 2px;
}

.switch label {
  cursor: pointer;
  color: rgba(0, 0, 0, 0.3);
  width: 60px;
  line-height: 50px;
  transition: all 0.2s ease;
}

label[for="map"] {
  position: absolute;
  left: 0px;
  bottom: -10px;
  font-size: small;
}

label[for="tile"] {
  position: absolute;
  right: 0px;
  bottom: -10px;
  font-size: small;
}

#tile:checked ~ .switch {
  background: var(--red-col);
}

#tile:checked ~ .switch span {
  background: white;
  margin-left: -8px;
}

#tile:checked ~ .switch span:after {
  background: white;
  height: 20px;
  margin-top: -8px;
  margin-left: 8px;
}

#map:checked ~ .switch label[for="map"] {
  color: white;
}

#tile:checked ~ .switch label[for="tile"] {
  color: white;
}
</style>
