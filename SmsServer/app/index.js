import {app} from './app.js';
import config from './config.js';

const PORT = config.PORT;

app.listen(PORT, () => {
    console.log(`Listen on port ${PORT}`);
});