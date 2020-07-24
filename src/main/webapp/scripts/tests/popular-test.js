import { addRegionOptions } from '../popular.js';

const mockGetRegionsResponse = {
  items: [
    { snippet: { gl: 'EG', name: 'Example'}}
  ]
}

QUnit.test('region menu display',  function(assert) {
  const done = assert.async();
  const getRegions = sinon.fake.returns(
    Promise.resolve(mockGetRegionsResponse)
  );
  addRegionOptions().then(() => {
    assert.dom('option').hasText('Example');
    done();
  });
});
