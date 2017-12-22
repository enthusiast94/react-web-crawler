import React from 'react';
import { shallow } from 'enzyme';
import App from './App';

describe('App', () => {
    
    it('should show "Hello world!" text', () => {
        const wrapper = shallow(<App />);
        const h1s = wrapper.find("h1");
        expect(h1s).toHaveLength(1);
        expect(h1s.at(0).text()).toBe("Hello world!");
    });
    
});
