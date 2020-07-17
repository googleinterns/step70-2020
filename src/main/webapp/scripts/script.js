function makeRegionSelect(snippet){
  const regionSelectDOM = document.getElementById('region');
  const option = document.createElement('option');
  option.value = snippet.gl;
  option.innerText = snippet.name;
  regionSelectDOM.appendChild(option);
}
