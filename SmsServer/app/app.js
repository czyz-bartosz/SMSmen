import express from 'express';
import apiRouter from './routes/api.router.js';

export {
    app
}

const app = express();

app.use('/api/', apiRouter);