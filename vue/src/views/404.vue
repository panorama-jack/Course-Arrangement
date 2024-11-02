<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <h1>404</h1>
      <p>Oops! Looks like you're lost.</p>
      <router-link to="home">Go Home</router-link>
    </div>
  </div>
</template>


<script>
export default {
  name: "NotFound"
}
</script>

<style scoped>
.not-found-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: linear-gradient(to right, #4d94ff, #0066cc);
  color: #fff;
  font-family: 'Arial', sans-serif;
}

.not-found-content {
  text-align: center;
}


h1 {
  font-size: 5em;
  margin: 10px 0;
  color: #ff6347;
}

p {
  font-size: 1.8em;
  margin-bottom: 20px;
}

</style>
