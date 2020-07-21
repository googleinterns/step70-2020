const VIDEO_LINK = "https://www.youtube.com/watch?v=testvideoid";
const SCORE = 0.05;

QUnit.module('Updating DOM after analyzing video');
QUnit.testStart(() => {
  document.getElementById('video-url').innerText = VIDEO_LINK;
})

QUnit.test('DOM updates with sentiment score', function (assert) {
  const fetchStub = sinon.stub(window, 'fetch');
  const body = { "score": SCORE, "dataAvailable": true };
  const response = { json: () => { return body }, status: 200 };
  fetchStub.returns(Promise.resolve(response));

  const done = assert.async()
  analyzeVideo();
  setTimeout(() => {
    assert.dom('#sentiment-container')
        .hasProperty('innerText',SCORE.toString());
    fetch.restore();
    done();
  });
});

QUnit.test('DOM updates with 400 error', function (assert) {
  const fetchStub = sinon.stub(window, 'fetch');
  const response = { status: 400, statusText: '400 error' };
  fetchStub.returns(Promise.resolve(response));

  const done = assert.async();
  analyzeVideo();
  setTimeout(() => {
    assert.dom('#sentiment-container').hasProperty('innerText','400 error');
    fetch.restore();
    done();
  });
});

QUnit.test('DOM updates with 500 error', function (assert) {
  const fetchStub = sinon.stub(window, 'fetch');
  const response = { status: 500, statusText: '500 error' };
  fetchStub.returns(Promise.resolve(response));

  const done = assert.async();
  analyzeVideo();
  setTimeout(() => {
    assert.dom('#sentiment-container').hasProperty('innerText','500 error');
    fetch.restore();
    done();
  });
});

QUnit.test('DOM updates without score', function (assert) {
  const fetchStub = sinon.stub(window, 'fetch');
  const body = { "dataAvailable": false };
  const response = { json: () => { return body }, status: 200 };
  fetchStub.returns(Promise.resolve(response));

  const done = assert.async()
  analyzeVideo();
  setTimeout(() => {
    assert.dom('#sentiment-container')
        .hasProperty('innerText','We couldn\'t analyze this video! Try again.');
    fetch.restore();
    done();
  });
});