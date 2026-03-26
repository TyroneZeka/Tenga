import { useMutation, useQuery } from '@tanstack/react-query'
import * as paymentApi from '../api/paymentApi'
import type { InitiatePaymentRequest } from '../types'

export function useTransactions() {
  return useQuery({
    queryKey: ['transactions'],
    queryFn: paymentApi.getTransactions,
  })
}

export function useInitiatePayment() {
  return useMutation({
    mutationFn: (data: InitiatePaymentRequest) => {
      if (data.method === 'ECOCASH') return paymentApi.initiateEcoCash(data)
      return paymentApi.initiateInnBucks(data)
    },
  })
}
