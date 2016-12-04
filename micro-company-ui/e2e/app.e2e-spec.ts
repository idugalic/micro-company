import { MicroCompanyUiPage } from './app.po';

describe('micro-company-ui App', function() {
  let page: MicroCompanyUiPage;

  beforeEach(() => {
    page = new MicroCompanyUiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('micro-company');
  });
});
