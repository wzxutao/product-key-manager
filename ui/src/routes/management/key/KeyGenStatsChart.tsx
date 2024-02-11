import * as React from 'react';
import { PieChart } from '@mui/x-charts/PieChart';
import { KeyGenStats } from '../../../http/admin-api';
import { stat } from 'fs';
import { CircularProgress, Container } from '@mui/material';

export default function KeyGenStatsChart(props: {
  stats: KeyGenStats | null
}) {
  const { stats } = props;

  return (
    <Container sx={{margin: '8px auto'}}>
      {
        stats === null ? <CircularProgress /> :
          <PieChart
            series={[
              {
                data: [
                  { id: 0, value: stats.usedKeyCount, label: '已使用: ' + stats.usedKeyCount },
                  { id: 1, value: stats.remainingKeyCount, label: '剩余: ' + stats.remainingKeyCount },
                  { id: 2, value: stats.blackListedKeyCount, label: '黑名单: ' + stats.blackListedKeyCount },
                ],
                highlightScope: { faded: 'global', highlighted: 'item' },
                faded: { innerRadius: 30, additionalRadius: -30, color: 'gray' },
              },
            ]}
            height={200}
          />
      }
    </Container>
  );
}